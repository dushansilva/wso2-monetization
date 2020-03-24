import React, { useContext, useState, useEffect } from 'react';
import { ApiContext } from 'AppComponents/Apis/Details/ApiContext';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import Grid from '@material-ui/core/Grid';
import CircularProgress from '@material-ui/core/CircularProgress';
import { FormattedMessage, injectIntl } from 'react-intl';
import Alert from 'AppComponents/Shared/Alert';
import Progress from 'AppComponents/Shared/Progress';
import queryString from 'query-string';
import Monetization from '../data/Monetization';
import PaymentDetailsBase from './PaymentDetailsBase';

const PaymentDetails = (props) => {
    const { api } = useContext(ApiContext);
    const [isRedirecting, setIsRedirecting] = useState(false);
    const [loading, setLoading] = useState(true);
    const [cardDetails, setCardDetails] = useState(null);
    useEffect(() => {
        const { sessionId } = queryString.parse(props.location.search);
        const monetization = new Monetization();
        const { intl } = props;
        if (sessionId) {
            monetization
                .completeUpdateCardSession(sessionId)
                .then((response) => {
                    setLoading(false);
                    Alert.info(intl.formatMessage({
                        defaultMessage: 'Card details have been updated successfully',
                        id: 'monetization.CustomComponent.PaymentDetails.card.updated.successfully',
                    }));
                    return monetization.getCardDetails();
                })
                .then((cardRes) => {
                    setCardDetails(cardRes.obj);
                })
                .catch((error) => {
                    console.log('Error while upating card.');
                    console.log(error);
                    Alert.error(intl.formatMessage({
                        defaultMessage: 'Failed to update card details. Please try again.',
                        id: 'monetization.CustomComponent.PaymentDetails.card.update.failed',
                    }));
                    setLoading(false);
                });
        } else {
            monetization
                .getCardDetails()
                .then((response) => {
                    setCardDetails(response.obj);
                    setLoading(false);
                })
                .catch((error) => {
                    console.log('Error while upating card.');
                    console.log(error);
                    const { status } = error;
                    let message = 'Failed to fetch card details. Please try again.';
                    if (status === 404) {
                        message = 'No card details found';
                    }
                    Alert.error(intl.formatMessage({
                        defaultMessage: message,
                        id: 'monetization.CustomComponent.PaymentDetails.card.update.failed',
                    }));
                    setLoading(false);
                });
        }
    }, []);

    const redirectToUpdateCard = () => {
        setIsRedirecting(true);
        const monetization = new Monetization();
        monetization.getUpdateCardSession(api.id)
            .then((response) => {
                const { sessionId, stripePublishableKey } = response.body;
                const stripe = Stripe(stripePublishableKey);
                const { error } = stripe.redirectToCheckout({
                    sessionId,
                });
                if (error) {
                    console.log('Error while creating redirect.');
                    console.error(error);
                    setIsRedirecting(false);
                }
            })
            .catch((error) => {
                console.log('Error while settiping update card session.');
                console.log(error);
                setIsRedirecting(false);
            });
    };
    if (loading) {
        return <Progress />;
    }
    console.log(cardDetails);
    if (cardDetails) {
        return (
            <PaymentDetailsBase title={
                <Typography variant='h5'>
                    <FormattedMessage
                        id='monetization.CustomComponent.PaymentDetails.payment.details'
                        defaultMessage='Payment Details'
                    />
                </Typography>
            }
            >
                <Box py={1} mb={1} display='block'>
                    <Grid item xs={11} md={8}>
                        <Box display='flex' mt={1} ml={5}>
                            <Box display='block'>
                                <Typography variant='subtitle2'>
                                    <FormattedMessage
                                        id='monetization.CustomComponent.PaymentDetails.card.brand'
                                        defaultMessage='Card brand'
                                    />
                                </Typography>
                                <Typography variant='body1' >
                                    {cardDetails.brand}
                                </Typography>
                            </Box>
                            <Box display='block' ml={3}>
                                <Typography variant='subtitle2'>
                                    <FormattedMessage
                                        id='monetization.CustomComponent.PaymentDetails.last.4'
                                        defaultMessage='last 4'
                                    />
                                </Typography>
                                <Typography variant='body1' >
                                    {cardDetails.last4}
                                </Typography>
                            </Box>
                        </Box>
                        <Box display='flex' ml={5} mt={1}>
                            <Box display='block'>
                                <Typography variant='subtitle2'>
                                    <FormattedMessage
                                        id='monetization.CustomComponent.PaymentDetails.last.4'
                                        defaultMessage='Expiry Month'
                                    />
                                </Typography>
                                <Typography variant='body1' >
                                    {cardDetails.expMonth}
                                </Typography>
                            </Box>
                            <Box display='block' ml={3}>
                                <Typography variant='subtitle2'>
                                    <FormattedMessage
                                        id='monetization.CustomComponent.PaymentDetails.last.4'
                                        defaultMessage='Expiry year'
                                    />
                                </Typography>
                                <Typography variant='body1' >
                                    {cardDetails.expYear}
                                </Typography>
                            </Box>
                        </Box>
                    </Grid>
                    <Grid item xs={11} md={8}>
                        <Box my={2} ml={5} display='flex'>
                            <Button
                                color='primary'
                                variant='contained'
                                size='small'
                                icon='edit'
                                disalbed={isRedirecting}
                                onClick={redirectToUpdateCard}
                            >
                                <FormattedMessage
                                    id='monetization.CustomComponent.PaymentDetails.update.card'
                                    defaultMessage='update card'
                                />
                                {isRedirecting && <CircularProgress size={24} />}
                            </Button>
                        </Box>
                    </Grid>
                </Box>
            </PaymentDetailsBase>);
    } else {
        return (
            <PaymentDetailsBase title={
                <Typography variant='h5'>
                    <FormattedMessage
                        id='monetization.CustomComponent.PaymentDetails.payment.details'
                        defaultMessage='No payment details found'
                    />
                </Typography>
            }
            />
        );
    }
};

PaymentDetails.propTypes = {
    history: PropTypes.shape({
        push: PropTypes.func.isRequired,
    }).isRequired,
    location: PropTypes.shape({
        search: PropTypes.string,
    }).isRequired,
    intl: PropTypes.shape({
        formatMessage: PropTypes.func.isRequired,
    }).isRequired,
};

export default injectIntl(PaymentDetails);
